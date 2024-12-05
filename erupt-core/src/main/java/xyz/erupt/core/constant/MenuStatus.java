package xyz.erupt.core.constant;

import lombok.Getter;
import xyz.erupt.annotation.fun.ChoiceFetchHandler;
import xyz.erupt.annotation.fun.VLModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author YuePeng
 * date 2021/4/19 10:48
 */
@Getter
public enum MenuStatus {
    OPEN(1, "启用"), HIDE(2, "隐藏"), DISABLE(3, "禁用");

    private final int value;
    private final String label;

    MenuStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static MenuStatus valueOf(int value) {
        for (MenuStatus menuStatus : MenuStatus.values()) {
            if (menuStatus.getValue() == value) {
                return menuStatus;
            }
        }
        return null;
    }

    public static class ChoiceFetch implements ChoiceFetchHandler {

        @Override
        public List<VLModel> fetch(String[] params) {
            return Stream.of(MenuStatus.values()).map(menuTypeEnum ->
                    new VLModel(menuTypeEnum.getValue() + "", menuTypeEnum.getLabel())).collect(Collectors.toList());
        }

    }
}
