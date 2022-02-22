package org.cnodejs.android.md.bus

import org.cnodejs.android.md.model.entity.Account

class AccountChangedEvent(val account: Account?)

class AccountUpdatedEvent(val account: Account)

class AccountInfoNeedRefreshEvent(
    val user: Boolean = false,
    val messagesCount: Boolean = false,
)
