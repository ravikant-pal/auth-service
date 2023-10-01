package com.triton.auth.service.impl;

import com.google.common.collect.Maps;
import com.triton.auth.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public TemplateServiceImpl(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String parseTemplate(String templateName, Map<String, Object> templateVariables) {
        Context context = new Context();
        if (CollectionUtils.isEmpty(templateVariables)) {
            context.setVariables(templateVariables);
        }
        return templateEngine.process(templateName, context);
    }

    @Override
    public String parseTemplate(String templateName) {
        return parseTemplate(templateName, Maps.newHashMap());
    }
}
