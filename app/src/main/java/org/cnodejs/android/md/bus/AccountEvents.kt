package org.cnodejs.android.md.bus

import org.cnodejs.android.md.model.entity.Account

data class AccountChangedEvent(val account: Account?)

data class AccountUpdatedEvent(val account: Account)
