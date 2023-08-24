package cc.cybereflex.media.onvif.command;

import cc.cybereflex.common.component.XmlParser;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.media.onvif.common.AbstractOnvifCommand;
import cc.cybereflex.media.onvif.model.OnvifDevice;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;

@RequiredArgsConstructor
public class GetRtspUriCommand extends AbstractOnvifCommand<String> {

    private final OnvifDevice onvifDevice;

    private final String profileToken;

    @Override
    protected URI uri() {
        return URI.create(onvifDevice.getMediaUrl());
    }

    @Override
    protected String body() {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope">
                    <s:Header>%s</s:Header>
                    <s:Body xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                        <GetStreamUri xmlns="http://www.onvif.org/ver10/media/wsdl">
                            <StreamSetup>
                                <Stream xmlns="http://www.onvif.org/ver10/schema">RTP-Unicast</Stream>
                                <Transport xmlns="http://www.onvif.org/ver10/schema">
                                    <Protocol>RTSP</Protocol>
                                </Transport>
                            </StreamSetup>
                            <ProfileToken>%s</ProfileToken>
                        </GetStreamUri>
                    </s:Body>
                </s:Envelope>
                """
                .formatted(
                        buildWssHeader(onvifDevice.getUsername(), onvifDevice.getPassword()),
                        profileToken
                );
    }

    @Override
    protected Result<String> processResponse(HttpResponse<byte[]> response) {
        Result<String> result = super.processResponse(response);
        if (result.isSuccess()) {
            XmlParser.parse(new ByteArrayInputStream(response.body()), new XmlParser.AbstractSAXHandler() {
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    Optional<XmlParser.Element> currentElement = peekCurrentElement();
                    currentElement.ifPresent(it -> {
                        if (StringUtils.equals(it.getLocalName(), "Uri")) {
                            result.setData(new String(ch, start, length));
                        }
                    });
                }
            });


        }

        return result;
    }
}