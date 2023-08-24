package cc.cybereflex.media.server.codec.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SipResponse extends SipMessage {

    private SipResponseStatus responseStatus;

}
