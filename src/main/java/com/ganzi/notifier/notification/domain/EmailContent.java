package com.ganzi.notifier.notification.domain;

public record EmailContent(String subject, String body) implements Content {
}
