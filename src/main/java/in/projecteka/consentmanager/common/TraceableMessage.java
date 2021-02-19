package in.projecteka.consentmanager.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TraceableMessage implements Serializable {
    String correlationId;
    Object message;
}
