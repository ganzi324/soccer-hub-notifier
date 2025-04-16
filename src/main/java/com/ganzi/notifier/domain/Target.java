package com.ganzi.notifier.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ganzi.notifier.domain.email.EmailReceiver;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailReceiver.class, name = "EMAIL")
})
public interface Target {
}
