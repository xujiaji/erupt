package xyz.erupt.annotation.sub_field.sub_edit;

import xyz.erupt.annotation.config.Comment;

import java.beans.Transient;

/**
 * @author YuePeng
 * date 2019-01-23.
 */
public @interface AttachmentType {

    @Comment("附件大小限制，单位KB")
    long size() default -1;

    @Transient
    @Comment("定义独享存储空间，便于文件查找")
    String path() default "";

    @Comment("允许上传的文件类型")
    String[] fileTypes() default {};

    @Comment("附件类型")
    Type type() default Type.BASE;

    @Comment("最大上传数")
    int maxLimit() default 1;

    @Transient
    ImageType imageType() default @ImageType;

    @Comment("多张图片分割字符")
    String fileSeparator() default "|";

    enum Type {
        @Comment("可上传任意类型文件")
        BASE,
        @Comment("图片上传")
        IMAGE,
    }

    @interface ImageType {

        //最小宽度限制
        int minWidth() default 0;

        //最大宽度限制
        int maxWidth() default Integer.MAX_VALUE;

        //最小高度限制
        int minHeight() default 0;

        //最大高度限制
        int maxHeight() default Integer.MAX_VALUE;

    }

}