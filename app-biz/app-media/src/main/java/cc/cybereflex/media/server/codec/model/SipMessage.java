package cc.cybereflex.media.server.codec.model;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.DecoderResultProvider;
import io.netty.util.internal.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class SipMessage implements DecoderResultProvider {

    private SipVersion sipVersion;
    private SipHeaders sipHeaders;
    private ByteBuf content;
    private DecoderResult decoderResult = DecoderResult.SUCCESS;


    @Override
    public DecoderResult decoderResult() {
        return decoderResult;
    }

    @Override
    public void setDecoderResult(DecoderResult decoderResult) {
        this.decoderResult = ObjectUtil.checkNotNull(decoderResult, "decoderResult");
    }
}
