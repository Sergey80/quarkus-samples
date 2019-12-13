package my.project.subject.infrastructure.web;

public class ErrorResponseData {

    private String errorCode;
    private String message;
    private String details;

    private ErrorResponseData(final String errorCode, final String message, final String details) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }

    private ErrorResponseData() {
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public static final class ErrorResponseDataBuilder {
        private String errorCode;
        private String message;
        private String details;

        public ErrorResponseDataBuilder() {
        }

        public ErrorResponseDataBuilder errorCode(final String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorResponseDataBuilder message(final String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseDataBuilder details(final String details) {
            this.details = details;
            return this;
        }

        public ErrorResponseData build() {
            return new ErrorResponseData(this.errorCode, this.message, this.details);
        }
    }

    public static ErrorResponseDataBuilder builder() {
        return new ErrorResponseDataBuilder();
    }
}
