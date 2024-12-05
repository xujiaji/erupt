package xyz.erupt.core.controller;

import org.springframework.web.bind.annotation.*;
import xyz.erupt.annotation.fun.VLModel;
import xyz.erupt.annotation.sub_field.sub_edit.AutoCompleteType;
import xyz.erupt.annotation.sub_field.sub_edit.ChoiceType;
import xyz.erupt.annotation.sub_field.sub_edit.CodeEditorType;
import xyz.erupt.core.annotation.EruptRouter;
import xyz.erupt.core.constant.EruptRestPath;
import xyz.erupt.core.exception.EruptApiErrorTip;
import xyz.erupt.core.exception.EruptWebApiRuntimeException;
import xyz.erupt.core.service.EruptCoreService;
import xyz.erupt.core.util.EruptSpringUtil;
import xyz.erupt.core.util.EruptUtil;
import xyz.erupt.core.view.EruptApiModel;
import xyz.erupt.core.view.EruptFieldModel;
import xyz.erupt.core.view.EruptModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author YuePeng
 * date 2020-08-14
 */
@RestController
@RequestMapping(EruptRestPath.ERUPT_COMP)
public class EruptComponentController {

    /**
     * autoComplete 组件联动接口
     *
     * @param field    自动完成组件字段
     * @param val      输入框的值
     * @param formData 完整表单对象
     * @return 联想结果
     */
    @PostMapping("/auto-complete/{erupt}/{field}")
    @EruptRouter(authIndex = 2, verifyType = EruptRouter.VerifyType.ERUPT)
    public List<Object> autoCompleteValue(@PathVariable("erupt") String eruptName,
                                          @PathVariable("field") String field,
                                          @RequestParam("val") String val,
                                          @RequestBody(required = false) Map<String, Object> formData) {
        EruptFieldModel fieldModel = EruptCoreService.getErupt(eruptName).getEruptFieldMap().get(field);
        AutoCompleteType autoCompleteType = fieldModel.getEruptField().edit().autoCompleteType();
        if (val.length() < autoCompleteType.triggerLength()) {
            throw new EruptWebApiRuntimeException("char length must >= " + autoCompleteType.triggerLength());
        }
        try {
            return EruptSpringUtil.getBean(autoCompleteType.handler()).completeHandler(formData, val, autoCompleteType.param());
        } catch (Exception e) {
            throw new EruptApiErrorTip(e.getMessage(), EruptApiModel.PromptWay.MESSAGE);
        }
    }

    //Gets the CHOICE component drop-down list
    @GetMapping("/choice-item/{erupt}/{field}")
    @EruptRouter(authIndex = 2, verifyType = EruptRouter.VerifyType.ERUPT)
    public List<VLModel> choiceItem(@PathVariable("erupt") String eruptName,
                                    @PathVariable("field") String field) {
        EruptModel eruptModel = EruptCoreService.getErupt(eruptName);
        EruptFieldModel fieldModel = eruptModel.getEruptFieldMap().get(field);
        return EruptUtil.getChoiceList(eruptModel, fieldModel.getEruptField().edit().choiceType());
    }

    @GetMapping("/choice-trigger/{erupt}/{field}")
    @EruptRouter(authIndex = 2, verifyType = EruptRouter.VerifyType.ERUPT)
    public Map<String, Object> choiceTrigger(@PathVariable("erupt") String eruptName,
                                             @PathVariable("field") String field,
                                             @RequestParam("val") Object val) {
        EruptModel eruptModel = EruptCoreService.getErupt(eruptName);
        EruptFieldModel fieldModel = eruptModel.getEruptFieldMap().get(field);
        ChoiceType choiceType = fieldModel.getEruptField().edit().choiceType();
        if (choiceType.trigger().isInterface()) {
            return Collections.emptyMap();
        } else {
            return EruptSpringUtil.getBean(choiceType.trigger()).trigger(val, choiceType.triggerParams());
        }
    }

    //Gets the TAGS component data
    @GetMapping("/tags-item/{erupt}/{field}")
    @EruptRouter(authIndex = 2, verifyType = EruptRouter.VerifyType.ERUPT)
    public List<String> tagsItem(@PathVariable("erupt") String eruptName,
                                 @PathVariable("field") String field) {
        EruptFieldModel fieldModel = EruptCoreService.getErupt(eruptName).getEruptFieldMap().get(field);
        return EruptUtil.getTagList(fieldModel.getEruptField().edit().tagsType());
    }

    //Gets the CodeEdit component hint data
    @GetMapping("/code-edit-hints/{erupt}/{field}")
    @EruptRouter(authIndex = 2, verifyType = EruptRouter.VerifyType.ERUPT)
    public List<String> codeEditHints(@PathVariable("erupt") String eruptName,
                                      @PathVariable("field") String field) {
        EruptFieldModel fieldModel = EruptCoreService.getErupt(eruptName).getEruptFieldMap().get(field);
        CodeEditorType codeEditType = fieldModel.getEruptField().edit().codeEditType();
        return EruptSpringUtil.getBean(codeEditType.hint()).hint(codeEditType.hintParams());
    }

}
