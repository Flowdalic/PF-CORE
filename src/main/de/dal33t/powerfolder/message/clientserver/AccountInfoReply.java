package de.dal33t.powerfolder.message.clientserver;

import com.google.protobuf.AbstractMessage;
import de.dal33t.powerfolder.d2d.D2DObject;
import de.dal33t.powerfolder.light.AccountInfo;
import de.dal33t.powerfolder.message.Message;
import de.dal33t.powerfolder.protocol.AccountInfoProto;
import de.dal33t.powerfolder.protocol.AccountInfoReplyProto;
import de.dal33t.powerfolder.protocol.ReplyStatusCodeProto;
import de.dal33t.powerfolder.security.Account;

public class AccountInfoReply extends Message implements D2DObject {
    private static final long serialVersionUID = 100L;

    private String replyCode;
    private ReplyStatusCode replyStatusCode;
    private Account account;
    private AccountInfo accountInfo;

    /**
     * Serialization constructor
     */
    public AccountInfoReply() {
    }

    public AccountInfoReply(String replyCode, ReplyStatusCode replyStatusCode) {
        this.replyCode = replyCode;
        this.replyStatusCode = replyStatusCode;
    }

    public AccountInfoReply(String replyCode, ReplyStatusCode replyStatusCode, Account account) {
        this.replyCode = replyCode;
        this.replyStatusCode = replyStatusCode;
        this.account = account;
    }

    public AccountInfoReply(String replyCode, ReplyStatusCode replyStatusCode, AccountInfo accountInfo) {
        this.replyCode = replyCode;
        this.replyStatusCode = replyStatusCode;
        this.accountInfo = accountInfo;
    }

    /**
     * Init from D2D message
     *
     * @param mesg Message to use data from
     **/
    public AccountInfoReply(AbstractMessage mesg) {
        initFromD2D(mesg);
    }

    public String getReplyCode() {
        return replyCode;
    }

    public void setReplyCode(String replyCode) {
        this.replyCode = replyCode;
    }

    public ReplyStatusCode getReplyStatusCode() {
        return replyStatusCode;
    }

    public void setReplyStatusCode(ReplyStatusCode replyStatusCode) {
        this.replyStatusCode = replyStatusCode;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    /**
     * initFromD2DMessage
     * Init from D2D message
     *
     * @param mesg Message to use data from
     * @author Christian Oberdörfer <oberdoerfer@powerfolder.com>
     **/
    @Override
    public void initFromD2D(AbstractMessage mesg) {
        if (mesg instanceof AccountInfoReplyProto.AccountInfoReply) {
            AccountInfoReplyProto.AccountInfoReply proto = (AccountInfoReplyProto.AccountInfoReply) mesg;
            this.replyCode = proto.getReplyCode();
            this.replyStatusCode = new ReplyStatusCode(proto.getReplyStatusCode());
            this.accountInfo = new AccountInfo(proto.getAccountInfo());
        }
    }

    /**
     * toD2D
     * Convert to D2D message
     *
     * @return Converted D2D message
     * @author Christian Oberdörfer <oberdoerfer@powerfolder.com>
     **/
    @Override
    public AbstractMessage toD2D() {
        AccountInfoReplyProto.AccountInfoReply.Builder builder = AccountInfoReplyProto.AccountInfoReply.newBuilder();
        builder.setClazzName(this.getClass().getSimpleName());
        builder.setReplyCode(this.replyCode);
        if (this.replyStatusCode != null)
            builder.setReplyStatusCode((ReplyStatusCodeProto.ReplyStatusCode) this.replyStatusCode.toD2D());
        // Send account as account info
        if (this.account != null) builder.setAccountInfo((AccountInfoProto.AccountInfo) this.account.toD2D());
        else if (this.accountInfo != null)
            builder.setAccountInfo((AccountInfoProto.AccountInfo) this.accountInfo.toD2D());
        return builder.build();
    }
}
