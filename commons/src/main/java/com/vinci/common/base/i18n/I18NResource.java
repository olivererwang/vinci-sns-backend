package com.vinci.common.base.i18n;

import com.google.common.collect.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 多语言支持
 * Created by tim@vinci on 15-1-28.
 */
public class I18NResource {
    private static final Logger log = LoggerFactory.getLogger(I18NResource.class);

    private Table<Locale,MessageType , ResourceBundle> cache;

    public I18NResource(List<Locale> localeList , List<MessageType> typeList) {
        Table<Locale,MessageType , ResourceBundle> table = HashBasedTable.create();
        for (Locale locale : localeList) {
            for (MessageType type : typeList) {
                try {
                    if (locale == null || type == null){
                        throw new IllegalArgumentException("locale or type is NULL");
                    }
                    ResourceBundle rb = ResourceBundle.getBundle("i18n." + type.name() + ".message", locale);
                    if (rb == null) {
                        throw new RuntimeException("");
                    }
                    table.put(locale,type,rb);
                }catch (Throwable e) {
                    log.error("读取多语言配置出错:locale:"+locale+",type:"+type,e);
                    throw e;
                }
            }
        }
        this.cache = ImmutableTable.copyOf(table);
    }


    public String getMessage(Locale locale, MessageType type , String key) {
        if (StringUtils.isEmpty(key) || type == null) {
            return null;
        }
        ResourceBundle rb = cache.get(locale,type);
        if (rb == null) {
            rb = cache.get(Locale.SIMPLIFIED_CHINESE,type);
        }
        return rb.getString(key);
    }

}
