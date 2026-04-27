# Task Manager Messaging Guide

Maintainer-oriented guide to the `messaging/` package in task-manager. Covers what lives here, what comes from the shared `shared-messaging` library, and step-by-step recipes for the work you'll most often do.

## TL;DR

- All RabbitMQ infrastructure (publisher, dispatcher, registries, properties, auto-config, topology DSL) lives in **`com.faisal.shared.messaging.*`** from the `shared-messaging` library, consumed via `mavenLocal()` as `com.faisal:shared-messaging:0.0.1-SNAPSHOT`.
- This package keeps only what's specific to task-manager: event-type constants, payload records, the inbound handler that completes pending requests, the queue-edge listener, and the topology declaration. The synchronous request/reply gateway and its timeout knob live in `task/`.
- Active flow: task creation calls `TaskAssigneeValidationGateway` → publishes `task.assignee.validation.request` → waits for `task.assignee.validation.result` → keeps or clears the assignee accordingly.

## Common Maintenance Tasks

### Add a new event that task-manager publishes after a local commit

Use this when a task-domain write should produce a fire-and-forget integration event.

1. Add a domain event class implementing `com.faisal.shared.messaging.outbound.OutboundDomainEvent` somewhere in the task domain (e.g. `task/event/TaskCreatedDomainEvent.java`).
2. Add the outbound payload record under `messaging/outbound/` (e.g. `TaskCreatedPayload`).
3. Add the event-type constant — extend `TaskAssigneeValidationEvents.java` style or create a new `<Feature>Events.java`.
4. Register an `@Bean OutboundEventDefinition<TaskCreatedDomainEvent, TaskCreatedPayload>` in a `@Configuration` class via `OutboundEventDefinition.of(...)`.
5. Publish the **internal** domain event from the task service (e.g. via `ApplicationEventPublisher`). The shared `TransactionalOutboundEventPublisher` translates it after commit.
6. Add a unit test for the mapping.

No topology change needed — the publisher writes to the shared exchange; consumers own the queue declarations on their side.

### Add a new synchronous request/reply flow (task-manager waits for an answer)

The assignee-validation flow is the reference pattern.

1. Add request and reply payloads (request under `messaging/outbound/`, reply under `messaging/inbound/`).
2. Add event-type constants in a `<Feature>Events.java`.
3. Add a coordinator/gateway under `task/` (or wherever the feature lives) — own the correlation id, pending-future map, timeout, and fallback. Inject `IntegrationEventPublisher`, `MessagingProperties` (for the enabled flag), `MessagingMetadata`, and a service-specific `<Feature>Properties` for the timeout.
4. Bind the timeout under a service-owned namespace (mirror `TaskAssigneeValidationProperties` bound to `task-manager.assignee-validation`) — **do not** add it to the shared `messaging.*` block.
5. Add an inbound handler implementing `InboundEventHandler<ReplyPayload>` that calls `gateway.completeValidation(event.correlationId(), event.payload())`.
6. Add the reply queue via `messagingTopology.queueWithDlq("task-manager.<flow-name>", REPLY_EVENT_TYPE)`.
7. Add a thin `@RabbitListener`-annotated bean delegating to `RabbitInboundEventDispatcher`.
8. Tests: gateway test (mirror `TaskAssigneeValidationGatewayTest`), handler test, topology test.

### Add a new inbound event (task-manager consumes from another service)

1. Add the inbound payload record under `messaging/inbound/`.
2. Add a typed handler implementing `InboundEventHandler<TPayload>`. Annotate with `@Component`.
3. If the event belongs to a new queue, add a `@Bean Declarables ...()` method (in a `@ConditionalOnProperty(prefix="messaging", name="enabled", havingValue="true")` `@Configuration`) that calls `messagingTopology.queueWithDlq("task-manager.<flow-name>", <routingKey>)`.
4. If the event belongs to an existing queue, no topology change is needed — the dispatcher routes by event type.
5. Add a thin `@RabbitListener` bean (mirror `AssigneeValidationResultsRabbitListener`) that delegates to `RabbitInboundEventDispatcher`.
6. Add a handler unit test and a topology test if you added a new queue.

### Add a new queue

Inside a `@Configuration` class gated on `messaging.enabled`:

```java
@Bean
Declarables myFlowDeclarables(MessagingTopology topology) {
    return topology.queueWithDlq("task-manager.<flow-name>", "<routing-key>");
}
```

The DSL produces: main exchange + DLX + main queue (with `x-dead-letter-*` args) + DLQ + bindings. Queue names follow `<base>.q` and `<base>.dlq`.

For `@RabbitListener(queues = ...)`, declare the queue name as `<BASE> + MessagingTopology.QUEUE_SUFFIX` — annotation arguments must be compile-time constants, so concatenation of two string literals works while a static method call doesn't.

### Modify the active assignee-validation flow

- **Change the timeout**: edit `task-manager.assignee-validation.timeout-ms` in `application.yaml` (or set `MESSAGING_ASSIGNEE_VALIDATION_TIMEOUT_MS`). The property is bound by `task/TaskAssigneeValidationProperties.java`.
- **Change fallback behavior** when the user is missing or the reply times out: `task/TaskAssigneeValidationGateway.java` — the warning strings and the `TaskAssigneeValidationOutcome` shape.
- **Change the request payload shape** sent by task-manager: keep field names compatible with user-manager's copy of the same record (drift fails silently at runtime).
- **Change the reply payload shape** received from user-manager: same — both sides own a local copy of the record; rename in lockstep.
- **Change the routing key**: update `TaskAssigneeValidationEvents.REQUEST_EVENT_TYPE`/`RESULT_EVENT_TYPE` (and the matching constants in user-manager) and re-deploy both services together.

### Override a piece of shared messaging infrastructure

Every `@Bean` in `RabbitMessagingAutoConfiguration` carries `@ConditionalOnMissingBean`, so to override (e.g. swap the publisher) just declare your own `@Bean` of that type in this service. Reach for this only when there's a real reason — most of the time you should be modifying the library instead.

### Change broker/connection settings

All knobs live under the `messaging.*` block in `application.yaml`. Schema is owned by `MessagingProperties` in the shared library. Add new infra-level properties **in the library**, not here. Service-specific feature knobs (like the assignee-validation timeout) belong in a service-owned `@ConfigurationProperties` class under a service-specific prefix.

## Current Active Flow

1. `TaskService#createTask` asks `TaskAssigneeValidationGateway` to validate the assignee when the request contains `assigneeId`.
2. The gateway publishes `task.assignee.validation.request` through `IntegrationEventPublisher`.
3. `user-manager` checks user existence and replies with `task.assignee.validation.result`.
4. `AssigneeValidationResultsRabbitListener` hands the reply to `RabbitInboundEventDispatcher`.
5. `TaskAssigneeValidationResultInboundHandler` completes the pending request in the gateway by correlation id.
6. The gateway returns one of three normalized outcomes:
   - keep the requested assignee
   - create the task with `assigneeId = null` and a "user not found" warning
   - create the task with `assigneeId = null` and a "validation unavailable" warning (timeout / messaging disabled / unhandled error)

If the create request has no `assigneeId`, task-manager skips messaging and persists the task directly.

## Responsibility Map

### Provided by `shared-messaging` (not in this package)

| Class | Purpose |
|---|---|
| `com.faisal.shared.messaging.api.IntegrationEventPublisher` | Publish abstraction used by application code |
| `com.faisal.shared.messaging.model.{IntegrationEvent,EventEnvelope,DefaultIntegrationEvent}` | Wire and outbound contracts |
| `com.faisal.shared.messaging.config.{MessagingProperties,MessagingMetadata}` | Externalized settings and service identity |
| `com.faisal.shared.messaging.inbound.{InboundEventHandler,InboundEventHandlerRegistry}` | Typed inbound contract and registry |
| `com.faisal.shared.messaging.outbound.{OutboundDomainEvent,OutboundEventDefinition,OutboundEventDefinitionRegistry,TransactionalOutboundEventPublisher}` | After-commit outbound translation |
| `com.faisal.shared.messaging.rabbit.listener.RabbitInboundEventDispatcher` | Envelope deserialization, handler lookup, typed dispatch |
| `com.faisal.shared.messaging.rabbit.topology.MessagingTopology` | Topology DSL: `queueWithDlq(baseName, routingKey)` |
| `com.faisal.shared.messaging.autoconfigure.MessagingAutoConfiguration` | Always-on wiring (no-op publisher, metadata, registries) |
| `com.faisal.shared.messaging.autoconfigure.RabbitMessagingAutoConfiguration` | Broker wiring, gated on `messaging.enabled=true` |

All auto-wire as soon as the dependency is on the classpath.

### Service-local — this package

| File | Purpose |
|---|---|
| `TaskAssigneeValidationEvents.java` | Request/reply event-type constants for the active flow |
| `outbound/TaskAssigneeValidationRequestPayload.java` | Outbound payload sent to user-manager |
| `inbound/TaskAssigneeValidationResultPayload.java` | Inbound payload received from user-manager |
| `inbound/TaskAssigneeValidationResultInboundHandler.java` | Completes the pending request in the gateway |
| `rabbit/listener/AssigneeValidationResultsRabbitListener.java` | Thin queue-edge listener that delegates to the shared dispatcher |
| `rabbit/topology/TaskManagerRabbitTopology.java` | Declares this service's reply queue + DLQ + bindings via `MessagingTopology.queueWithDlq(...)` |

### Service-local — `task/` (request/reply coordination)

| File | Purpose |
|---|---|
| `task/TaskAssigneeValidationGateway.java` | Owns correlation id, pending-future map, timeout handling, fallback |
| `task/TaskAssigneeValidationOutcome.java` | Normalized result returned to `TaskService` |
| `task/TaskAssigneeValidationProperties.java` | Service-specific `task-manager.assignee-validation.*` properties (timeout) |

## Maintenance Rules

- Don't inject `RabbitTemplate` into task-domain services — go through `IntegrationEventPublisher`.
- Don't put `@RabbitListener` methods on `@Configuration` classes — keep them on dedicated `@Component` listeners.
- Don't hardcode producer names — read from `MessagingMetadata`.
- Don't copy code out of `shared-messaging` to "tweak it" locally — propose the change in the library.
- Don't reintroduce one dedicated `*IntegrationEvent` class per event unless that event has genuinely custom metadata behavior — `DefaultIntegrationEvent` is the default vehicle.
- Don't add service-specific feature knobs (timeouts, retry counts that mean something only to one flow) under the shared `messaging.*` namespace — bind them in the service under a service-owned prefix.
- A missing `OutboundEventDefinition` registration for a domain event you're publishing is a configuration bug, not a no-op.

## Build & Run

The shared library is consumed from `mavenLocal()`. Before building this service for the first time (or after any change to the library), run:

```
cd microservices-playground/libraries/shared-messaging
./gradlew publishToMavenLocal
```

For Docker builds, the service `Dockerfile` uses `context: ../..` and copies `libraries/shared-messaging/` into the image, where it runs `gradle publishToMavenLocal` before building the service. The host `~/.m2` is not involved.

## Troubleshooting

- **Two `IntegrationEventPublisher` beans defined** — something in this service is also defining one. The auto-config registers either the no-op (when `messaging.enabled=false`) or the Rabbit-backed publisher. Remove the duplicate.
- **Assignee validation always falls back to "validation unavailable"** — check `messaging.enabled`, that RabbitMQ is up, that user-manager is up and bound to the request queue, and the correlation-id flow.
- **Validation always times out** — `task-manager.assignee-validation.timeout-ms` may be too low for the current latency, or user-manager's listener is failing silently. Check user-manager logs and the request DLQ.
- **Inbound replies ignored after adding a flow** — check `TaskManagerRabbitTopology` bindings, that the new handler is a Spring bean visible to component scan, and that `eventType()` matches the producer's routing key exactly.
- **`com.faisal:shared-messaging:0.0.1-SNAPSHOT` won't resolve** — the library hasn't been published to `mavenLocal` yet. See "Build & Run".
- **Docker still runs stale code** — rebuild the service image (`docker compose build task-manager`) before `docker compose up`.
- **`@RabbitListener(queues = ...)` complains about a non-constant expression** — make sure the queue-name constant is computed via string concatenation of literals (e.g. `<BASE> + MessagingTopology.QUEUE_SUFFIX`), not via a static method call.
