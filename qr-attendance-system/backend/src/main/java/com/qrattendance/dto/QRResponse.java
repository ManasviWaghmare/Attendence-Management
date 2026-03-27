package com.qrattendance.dto;

public class QRResponse {
    private String qrImage;
    private Long sessionId;
    private String token; // Encrypted token for testing/verification

    public QRResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final QRResponse response = new QRResponse();

        public Builder qrImage(String qrImage) {
            response.setQrImage(qrImage);
            return this;
        }

        public Builder sessionId(Long sessionId) {
            response.setSessionId(sessionId);
            return this;
        }

        public Builder token(String token) {
            response.setToken(token);
            return this;
        }

        public QRResponse build() {
            return response;
        }
    }

    public String getQrImage() { return qrImage; }
    public void setQrImage(String qrImage) { this.qrImage = qrImage; }
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
