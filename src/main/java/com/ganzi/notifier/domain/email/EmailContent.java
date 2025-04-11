package com.ganzi.notifier.domain.email;

import com.ganzi.notifier.domain.Content;

public record EmailContent(String subject, String body) implements Content {
}
