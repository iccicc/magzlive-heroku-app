package com.github.messenger4j.v3;

import static com.github.messenger4j.v3.RichMedia.Type.IMAGE;

import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.templates.Template;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author Max Grabenhorst
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
public final class Message {

    private final String text;
    private final RichMedia richMedia;
    private final Template template;
    private final List<QuickReply> quickReplies;
    private final String metadata;

    public static Message create(@NonNull String text) {
        return new Message(text, null, null, null, null);
    }

    public static Message create(@NonNull RichMedia richMedia) {
        return new Message(null, richMedia, null, null, null);
    }

    public static Message create(@NonNull Template template) {
        return new Message(null, null, template, null, null);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private Message(String text, RichMedia richMedia, Template template, List<QuickReply> quickReplies, String metadata) {
        this.text = text;
        this.richMedia = richMedia;
        this.template = template;
        this.quickReplies = quickReplies != null ? Collections.unmodifiableList(new ArrayList<>(quickReplies)) : null;
        this.metadata = metadata;
    }

    public String text() {
        return text;
    }

    public RichMedia richMedia() {
        return richMedia;
    }

    public Template template() {
        return template;
    }

    public List<QuickReply> quickReplies() {
        return quickReplies;
    }

    public String metadata() {
        return metadata;
    }

    public static final class Builder {
        private String text;
        private RichMedia richMedia;
        private Template template;
        private List<QuickReply> quickReplies;
        private String metadata;

        public Builder text(@NonNull String text) {
            if (richMedia != null || template != null) {
                throw new IllegalStateException("richMedia or template already set");
            }
            this.text = text;
            return this;
        }

        public Builder richMedia(@NonNull RichMedia richMedia) {
            if (text != null || template != null) {
                throw new IllegalStateException("text or template already set");
            }
            this.richMedia = richMedia;
            return this;
        }

        public Builder template(@NonNull Template template) {
            if (richMedia != null || text != null) {
                throw new IllegalStateException("richMedia or text already set");
            }
            this.template = template;
            return this;
        }

        public Builder quickReplies(@NonNull List<QuickReply> quickReplies) {
            this.quickReplies = quickReplies;
            return this;
        }

        public Builder metadata(@NonNull String metadata) {
            this.metadata = metadata;
            return this;
        }

        public Message build() {
            if (text == null && richMedia == null && template == null) {
                throw new IllegalStateException("At least text, or richMedia, or template must be set");
            }
            if (quickReplies != null && richMedia != null && richMedia.type() != IMAGE) {
                throw new IllegalStateException("Not allowed to set quickReplies when richMedia type is other than IMAGE");
            }
            return new Message(text, richMedia, template, quickReplies, metadata);
        }
    }
}