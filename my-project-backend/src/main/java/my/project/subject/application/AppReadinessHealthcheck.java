package my.project.subject.application;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

/**
 * Readiness
 *
 * Readiness probes are designed to let Kubernetes know when your app is ready to serve traffic.
 * Kubernetes makes sure the readiness probe passes before allowing a service to send traffic to the pod.
 * If a readiness probe starts to fail, Kubernetes stops sending traffic to the pod until it passes.
 *
 * Liveness
 *
 * Liveness probes let Kubernetes know if your app is alive or dead.
 * If you app is alive, then Kubernetes leaves it alone.
 * If your app is dead, Kubernetes removes the Pod and starts a new one to replace it.
 *
 * http://localhost:8080/swagger-ui.
 *
 * /health/ready and /health/live
 *
 * http://localhost:8080/my-project-service/health/ready
 */

@ApplicationScoped
@Readiness
public class AppReadinessHealthcheck implements HealthCheck {

    public HealthCheckResponse call() {
        // TODO: may do some extra checks
        return HealthCheckResponse.named("Application Health Check").up().build();
    }
}
