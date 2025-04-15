package com.ganzi.notifier.infra.messaging;

import java.util.List;

public record EmailTargetMessage(List<String> to, List<String> cc, List<String> bcc) {
}
