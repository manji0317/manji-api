package com.manji.file.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.manji.base.basic.UploadConst;
import com.manji.base.basic.entity.BaseEntity;
import com.manji.base.dto.UserImgDTO;
import com.manji.base.entity.SysUser;
import com.manji.base.error.BizException;
import com.manji.base.service.UserDetailServiceImpl;
import com.manji.file.utils.FileUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 文件上传服务类
 *
 * @author Bqd
 * @since 2024/6/4 0:23
 */
@Service
@Slf4j
public class FileService {

    @Resource
    private UserDetailServiceImpl userDetailService;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateUserImg(String userId, UserImgDTO userImgDTO) {
        String uploadType = userImgDTO.getUploadType();
        return Optional.ofNullable(userImgDTO.getFile())
                .map(file -> {
                    log.info("修改用户头像或背景开始，UserID: {} | 上传类型： {} ", userId, uploadType);

                    String imgPath;
                    try {
                        imgPath = FileUtil.uploadFile(file.getBytes(), Objects.requireNonNull(file.getOriginalFilename()), userImgDTO.getUploadType(), userId);
                    } catch (Exception e) {
                        throw new BizException("用户图片文件上传失败", e);
                    }

                    LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
                    switch (uploadType) {
                        case UploadConst.USER_AVATAR:
                            updateWrapper.set(SysUser::getAvatar, imgPath);
                            break;
                        case UploadConst.BACKGROUND:
                            updateWrapper.set(SysUser::getBackgroundImg, imgPath);
                            break;
                        default:
                            return ResponseEntity.badRequest().build();
                    }

                    userDetailService.update(updateWrapper.eq(BaseEntity::getId, userId));

                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
