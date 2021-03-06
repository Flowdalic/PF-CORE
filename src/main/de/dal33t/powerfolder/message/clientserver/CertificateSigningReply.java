package de.dal33t.powerfolder.message.clientserver;

import com.google.protobuf.AbstractMessage;
import de.dal33t.powerfolder.d2d.D2DReplyMessage;
import de.dal33t.powerfolder.protocol.CertificateSigningReplyProto;
import de.dal33t.powerfolder.protocol.ReplyStatusCodeProto;

public class CertificateSigningReply extends D2DReplyMessage {

    private String certificate;

    public CertificateSigningReply() {
    }

    public CertificateSigningReply(String replyCode, ReplyStatusCode replyStatusCode) {
        this.replyCode = replyCode;
        this.replyStatusCode = replyStatusCode;
    }

    public CertificateSigningReply(String replyCode, ReplyStatusCode replyStatusCode, String certificate) {
        this.replyCode = replyCode;
        this.replyStatusCode = replyStatusCode;
        this.certificate = certificate;
    }

    /**
     * Init from D2D message
     *
     * @param mesg Message to use data from
     **/
    public CertificateSigningReply(AbstractMessage mesg) {
        initFromD2D(mesg);
    }

    /**
     * Init from D2D message
     *
     * @param message Message to use data from
     **/
    @Override
    public void initFromD2D(AbstractMessage message) {
        if (message instanceof CertificateSigningReplyProto.CertificateSigningReply) {
            CertificateSigningReplyProto.CertificateSigningReply proto = (CertificateSigningReplyProto.CertificateSigningReply) message;
            this.replyCode = proto.getReplyCode();
            this.replyStatusCode = new ReplyStatusCode(proto.getReplyStatusCode());
            this.certificate = proto.getCertificate();
        }
    }

    /**
     * Convert to D2D message
     *
     * @return Converted D2D message
     **/
    @Override
    public AbstractMessage toD2D() {
        CertificateSigningReplyProto.CertificateSigningReply.Builder builder = CertificateSigningReplyProto.CertificateSigningReply.newBuilder();
        builder.setClazzName(this.getClass().getSimpleName());
        if (this.replyCode != null) builder.setReplyCode(this.replyCode);
        if (this.replyStatusCode != null) builder.setReplyStatusCode((ReplyStatusCodeProto.ReplyStatusCode) this.replyStatusCode.toD2D());
        if (this.certificate != null) builder.setCertificate(this.certificate);
        return builder.build();
    }

}

