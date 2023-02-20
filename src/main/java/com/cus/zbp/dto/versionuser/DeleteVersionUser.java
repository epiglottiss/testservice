package com.cus.zbp.dto.versionuser;

import lombok.*;

public class DeleteVersionUser {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private long versionId;

        private long userId;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private long versionId;
        private String versionName;
        private String softwareName;

        private long userId;
        private String userEamil;

        public static DeleteVersionUser.Response from(VersionUserDto dto) {
            return DeleteVersionUser.Response.builder()
                    .versionId(dto.getVersionId()).versionName(dto.getVersionName())
                    .softwareName(dto.getSoftwareName())
                    .userId(dto.getUserId())
                    .userEamil(dto.getUserEamil())
                    .build();
        }
    }
}
