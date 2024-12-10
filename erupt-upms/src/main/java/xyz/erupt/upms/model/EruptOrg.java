package xyz.erupt.upms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.EruptI18n;
import xyz.erupt.annotation.constant.AnnotationConst;
import xyz.erupt.annotation.fun.DataProxy;
import xyz.erupt.annotation.query.Condition;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.core.exception.EruptWebApiRuntimeException;
import xyz.erupt.core.i18n.I18nTranslate;
import xyz.erupt.jpa.model.BaseModel;
import xyz.erupt.upms.service.EruptUserService;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author YuePeng
 * date 2020-04-08
 */
@Entity
@Table(name = "e_upms_org")
@Erupt(
        name = "组织管理",
        tree = @Tree(pid = "parentOrg.id", expandLevel = 5),
        dataProxy = EruptOrg.Comp.class,
        orderBy = "EruptOrg.sort asc"
)
@EruptI18n
@Getter
@Setter
@NoArgsConstructor
public class EruptOrg extends BaseModel {

    @Column(length = AnnotationConst.CODE_LENGTH, unique = true)
    @EruptField(
            views = @View(title = "组编码", sortable = true),
            edit = @Edit(title = "组编码", notNull = true, search = @Search(vague = true))
    )
    private String code;

    @EruptField(
            views = @View(title = "组名称", sortable = true),
            edit = @Edit(title = "组名称", notNull = true, search = @Search(vague = true))
    )
    private String name;

    @ManyToOne
    @EruptField(
            edit = @Edit(
                    title = "上级组",
                    type = EditType.REFERENCE_TREE,
                    referenceTreeType = @ReferenceTreeType(pid = "parentOrg.id", expandLevel = 3)
            )
    )
    private EruptOrg parentOrg;

    @EruptField(
            edit = @Edit(
                    title = "显示顺序"
            )
    )
    private Integer sort;

    @Component
    static class Comp implements DataProxy<EruptOrg> {
        @Resource
        private EruptUserService eruptUserService;

        @Override
        public String beforeFetch(List<Condition> conditions) {
            EruptUser eruptUser = eruptUserService.getCurrentEruptUser();
            if (eruptUser.getIsAdmin()) return null;
            if (null == eruptUser.getEruptOrg()) {
                throw new EruptWebApiRuntimeException(eruptUser.getName() + " " + I18nTranslate.$translate("upms.no_bind_org"));
            } else {
                final Set<Long> orgIds = eruptUser.getOtherOrgs().stream().map(BaseModel::getId).collect(Collectors.toSet());
                orgIds.add(eruptUser.getEruptOrg().getId());
                String ids = orgIds.stream().map(String::valueOf).reduce((s, s2) -> s + "," + s2).get();
                return "EruptOrg.id IN (" + ids + ")";
            }
        }
    }

}
