package com.ganzi.notifier.domain.email;

import com.ganzi.notifier.domain.Target;

import java.util.List;

public record EmailReceiver(List<String> to, List<String> cc, List<String> bcc) implements Target {
}
