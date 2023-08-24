package cc.cybereflex.media.onvif.command;

import cc.cybereflex.common.component.XmlParser;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.media.onvif.common.AbstractOnvifCommand;
import cc.cybereflex.media.onvif.model.OnvifDevice;
import cc.cybereflex.media.onvif.model.OnvifDeviceProfile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class GetProfilesCommand extends AbstractOnvifCommand<List<OnvifDeviceProfile>> {

    private final OnvifDevice onvifDevice;

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
                        <GetProfiles xmlns="http://www.onvif.org/ver10/media/wsdl"></GetProfiles>
                    </s:Body>
                </s:Envelope>
                """
                .formatted(buildWssHeader(onvifDevice.getUsername(), onvifDevice.getPassword()));
    }

    @Override
    protected Result<List<OnvifDeviceProfile>> processResponse(HttpResponse<byte[]> response) {
        Result<List<OnvifDeviceProfile>> result = super.processResponse(response);
        if (result.isSuccess()) {
            List<OnvifDeviceProfile> profiles = new ArrayList<>();
            result.setData(profiles);
            XmlParser.parse(new ByteArrayInputStream(response.body()), new XmlParser.AbstractSAXHandler() {
                private OnvifDeviceProfile profile;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    super.startElement(uri, localName, qName, attributes);
                    if (StringUtils.equals(localName, "Profiles")) {
                        profile = new OnvifDeviceProfile();
                        profile.setToken(attributes.getValue("token"));
                    }

                    if (StringUtils.equals(localName, "AudioSourceConfiguration")) {
                        profile.setAudioSourceConfiguration(
                                OnvifDeviceProfile.AudioSourceConfiguration.builder()
                                        .sourceToken(attributes.getValue("token"))
                                        .build()
                        );
                    }

                    if (StringUtils.equals(localName, "AudioEncoderConfiguration")) {
                        profile.setAudioEncoderConfiguration(
                                OnvifDeviceProfile.AudioEncoderConfiguration.builder()
                                        .token(attributes.getValue("token"))
                                        .build()
                        );
                    }


                    if (StringUtils.equals(localName, "VideoSourceConfiguration")) {
                        profile.setVideoSourceConfiguration(
                                OnvifDeviceProfile.VideoSourceConfiguration.builder()
                                        .sourceToken(attributes.getValue("token"))
                                        .build()
                        );
                    }

                    if (StringUtils.equals(localName, "VideoEncoderConfiguration")) {
                        profile.setVideoEncoderConfiguration(
                                OnvifDeviceProfile.VideoEncoderConfiguration.builder()
                                        .token(attributes.getValue("token"))
                                        .build()
                        );
                    }
                }


                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    super.endElement(uri, localName, qName);
                    if (StringUtils.equals(localName, "Profiles") && Objects.nonNull(profile)) {
                        profiles.add(profile);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    Optional<XmlParser.Element> currentElement = peekCurrentElement();
                    currentElement.ifPresent(it -> {
                        String textContent = new String(ch, start, length);
                        if (StringUtils.equals(it.getLocalName(), "Name")) {
                            Assert.notNull(profile, "reading name element, profile object can't be null");
                            Optional<XmlParser.Element> parentElement = peekParentElement();
                            parentElement.ifPresent(element -> {
                                switch (element.getLocalName()) {
                                    case "Profiles" -> profile.setName(textContent);
                                    case "VideoSourceConfiguration" ->
                                            profile.getVideoSourceConfiguration().setName(textContent);
                                    case "VideoEncoderConfiguration" ->
                                            profile.getVideoEncoderConfiguration().setName(textContent);
                                    case "AudioSourceConfiguration" ->
                                            profile.getAudioSourceConfiguration().setName(textContent);
                                    case "AudioEncoderConfiguration" ->
                                            profile.getAudioEncoderConfiguration().setName(textContent);

                                }
                            });
                        }


                        if (StringUtils.equals(it.getLocalName(), "UseCount")) {
                            Assert.notNull(profile, "reading UseCount element, profile object can't be null");
                            Optional<XmlParser.Element> parentElement = peekParentElement();
                            parentElement.ifPresent(element -> {
                                switch (element.getLocalName()) {
                                    case "VideoSourceConfiguration" ->
                                            profile.getVideoSourceConfiguration().setUseCount(textContent);
                                    case "AudioSourceConfiguration" ->
                                            profile.getAudioSourceConfiguration().setUseCount(textContent);
                                }
                            });
                        }


                        if (StringUtils.equals(it.getLocalName(), "SourceToken")) {
                            Assert.notNull(profile, "reading SourceToken element, profile object can't be null");
                            Optional<XmlParser.Element> parentElement = peekParentElement();
                            parentElement.ifPresent(element -> {
                                switch (element.getLocalName()) {
                                    case "VideoSourceConfiguration" ->
                                            profile.getVideoSourceConfiguration().setSourceToken(textContent);
                                    case "AudioSourceConfiguration" ->
                                            profile.getAudioSourceConfiguration().setSourceToken(textContent);
                                }
                            });
                        }


                        if (StringUtils.equals(it.getLocalName(), "Encoding")) {
                            Assert.notNull(profile, "reading Encoding element, profile object can't be null");
                            Optional<XmlParser.Element> parentElement = peekParentElement();
                            parentElement.ifPresent(element -> {
                                switch (element.getLocalName()) {
                                    case "VideoEncoderConfiguration" ->
                                            profile.getVideoEncoderConfiguration().setEncoding(textContent);
                                    case "AudioEncoderConfiguration" ->
                                            profile.getAudioEncoderConfiguration().setEncoding(textContent);
                                }
                            });
                        }

                        if (StringUtils.equals(it.getLocalName(), "SampleRate")) {
                            Assert.notNull(profile, "reading SampleRate element, profile object can't be null");
                            profile.getAudioEncoderConfiguration().setSampleRate(Integer.parseInt(textContent));
                        }


                        if (StringUtils.equals(it.getLocalName(), "Bitrate")) {
                            Assert.notNull(profile, "reading Bitrate element, profile object can't be null");
                            profile.getAudioEncoderConfiguration().setBitrate(Integer.parseInt(textContent));
                        }

                        if (StringUtils.equals(it.getLocalName(), "FrameRateLimit")) {
                            Assert.notNull(profile, "reading FrameRateLimit element, profile object can't be null");
                            profile.getVideoEncoderConfiguration().setFrameRate(Integer.parseInt(textContent));
                        }

                        if (StringUtils.equals(it.getLocalName(), "Width")) {
                            Assert.notNull(profile, "reading Width element, profile object can't be null");
                            profile.getVideoEncoderConfiguration().setWidth(Integer.parseInt(textContent));
                        }


                        if (StringUtils.equals(it.getLocalName(), "Height")) {
                            Assert.notNull(profile, "reading Height element, profile object can't be null");
                            profile.getVideoEncoderConfiguration().setHeight(Integer.parseInt(textContent));
                        }
                    });
                }

            });

        }

        return result;
    }
}
