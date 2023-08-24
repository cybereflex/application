package cc.cybereflex.media.onvif.command;

import cc.cybereflex.media.onvif.common.AbstractOnvifCommand;
import cc.cybereflex.media.onvif.model.OnvifDevice;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@RequiredArgsConstructor
public class DevicePtzCommand extends AbstractOnvifCommand<Void> {

    private final OnvifDevice onvifDevice;
    private final String profileToken;
    private final double x;
    private final double y;
    private final double zoom;

    @Override
    protected URI uri() {
        return URI.create(onvifDevice.getPtzUrl());
    }

    @Override
    protected String body() {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope">
                    <s:Header>%s</s:Header>
                    <s:Body xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                        <RelativeMove xmlns="http://www.onvif.org/ver20/ptz/wsdl">
                            <ProfileToken>%s</ProfileToken>
                            <Translation>
                                <PanTilt xmlns="http://www.onvif.org/ver10/schema" x="%s" y="%s" space="http://www.onvif.org/ver10/tptz/PanTiltSpaces/TranslationGenericSpace"></PanTilt>
                                <Zoom xmlns="http://www.onvif.org/ver10/schema" x="%s" space="http://www.onvif.org/ver10/tptz/ZoomSpaces/TranslationGenericSpace"></Zoom>
                            </Translation>
                        </RelativeMove>
                    </s:Body>
                </s:Envelope>
                """
                .formatted(
                        buildWssHeader(onvifDevice.getUsername(), onvifDevice.getPassword()),
                        profileToken,
                        x,
                        y,
                        zoom
                );
    }
}
