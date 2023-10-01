package com.triton.auth.dto.helper;

import com.google.common.collect.Maps;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.time.Year;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
public class Mail {
    private String from;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String body;
    private boolean containsTemplate;
    private Map<String, Object> templateVariable;
    //todo: make support for attachments


    private Mail(MailBuilder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.cc = builder.cc;
        this.bcc = builder.bcc;
        this.subject = builder.subject;
        this.body = builder.body;
        this.containsTemplate = builder.containsTemplate;
        this.templateVariable = builder.templateVariable;
    }

    public static MailBuilder getBuilder() {
        return new MailBuilder();
    }

    public static class MailBuilder {

        private String from;
        private String[] to;
        private String[] cc;
        private String[] bcc;
        private String subject;
        private String body;
        private boolean containsTemplate = false;
        private Map<String, Object> templateVariable;

        public MailBuilder from(String from) {
            this.from = from;
            return this;
        }

        public MailBuilder to(String... to) {
            this.to = to;
            return this;
        }

        public MailBuilder cc(String... cc) {
            this.cc = cc;
            return this;
        }

        public MailBuilder bcc(String... bcc) {
            this.bcc = bcc;
            return this;
        }

        public MailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder body(String body) {
            this.body = body;
            return this;
        }

        public MailBuilder containsTemplate(boolean containsTemplate) {
            if(containsTemplate) {
                if(CollectionUtils.isEmpty(templateVariable)) this.templateVariable = Maps.newHashMap();
                this.templateVariable.put("currentYear", Year.now().getValue());
            }
            this.containsTemplate = containsTemplate;
            return this;
        }

        public MailBuilder templateVariable(Map<String, Object> templateVariable) {
            if(CollectionUtils.isEmpty(templateVariable)) this.templateVariable = Maps.newHashMap();
            else this.templateVariable.putAll(templateVariable);
            return this;
        }

        public Mail build(){
            return new Mail(this);
        }
    }


}
