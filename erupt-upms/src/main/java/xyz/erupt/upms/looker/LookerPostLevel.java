package xyz.erupt.upms.looker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.PreDataProxy;
import xyz.erupt.annotation.config.EruptSmartSkipSerialize;
import xyz.erupt.annotation.fun.DataProxy;
import xyz.erupt.annotation.query.Condition;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.Readonly;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.DateType;
import xyz.erupt.core.context.MetaContext;
import xyz.erupt.core.exception.EruptWebApiRuntimeException;
import xyz.erupt.core.service.I18NTranslateService;
import xyz.erupt.jpa.model.BaseModel;
import xyz.erupt.upms.model.EruptUser;
import xyz.erupt.upms.model.EruptUserPostVo;
import xyz.erupt.upms.service.EruptUserService;

import javax.annotation.Resource;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author YuePeng
 * date 2021/3/10 11:30
 */
@MappedSuperclass
@PreDataProxy(LookerPostLevel.class)
@Component
@Getter
@Setter
public class LookerPostLevel extends BaseModel implements DataProxy<LookerPostLevel> {

    @ManyToOne
    @EruptField(
            views = {
                    @View(title = "创建人", column = "name"),
                    @View(title = "所属组织", column = "eruptOrg.name"),
                    @View(title = "岗位", column = "eruptPost.name"),
            },
            edit = @Edit(title = "创建人", readonly = @Readonly, type = EditType.REFERENCE_TABLE)
    )
    @EruptSmartSkipSerialize
    private EruptUserPostVo createUser;

    @EruptField(
            views = @View(title = "创建时间", sortable = true),
            edit = @Edit(title = "创建时间", readonly = @Readonly, dateType = @DateType(type = DateType.Type.DATE_TIME))
    )
    @EruptSmartSkipSerialize
    private Date createTime;

    @EruptSmartSkipSerialize
    private Date updateTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @EruptSmartSkipSerialize
    private EruptUser updateUser;

    @Resource
    @Transient
    private EruptUserService eruptUserService;

    @Resource
    @Transient
    private I18NTranslateService i18NTranslateService;

    @Override
    public String beforeFetch(List<Condition> conditions) {
        EruptUser eruptUser = eruptUserService.getCurrentEruptUser();
        if (eruptUser.getIsAdmin()) return null;
        if (null == eruptUser.getEruptOrg() || null == eruptUser.getEruptPost()) {
            throw new EruptWebApiRuntimeException(eruptUser.getName() + " " + i18NTranslateService.translate("未绑定的岗位无法查看数据"));
        }
        String eruptName = MetaContext.getErupt().getName();
        return "(" + eruptName + ".createUser.id = " + eruptUserService.getCurrentUid()
                + " or " + eruptName + ".createUser.eruptOrg.id = " + eruptUser.getEruptOrg().getId() + " and "
                + eruptName + ".createUser.eruptPost.weight < " + eruptUser.getEruptPost().getWeight() + ")";
    }

    @Override
    public void beforeAdd(LookerPostLevel lookerPostLevel) {
        lookerPostLevel.setCreateTime(new Date());
        lookerPostLevel.setCreateUser(new EruptUserPostVo(eruptUserService.getCurrentUid()));
    }

    @Override
    public void beforeUpdate(LookerPostLevel lookerPostLevel) {
        lookerPostLevel.setUpdateTime(new Date());
        lookerPostLevel.setUpdateUser(new EruptUser(eruptUserService.getCurrentUid()));
    }
}