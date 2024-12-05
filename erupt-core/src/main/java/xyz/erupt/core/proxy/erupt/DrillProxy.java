package xyz.erupt.core.proxy.erupt;

import org.aopalliance.intercept.MethodInvocation;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.constant.AnnotationConst;
import xyz.erupt.annotation.sub_erupt.Drill;
import xyz.erupt.core.proxy.AnnotationProxy;
import xyz.erupt.core.proxy.ProxyContext;

/**
 * @author YuePeng
 * date 2022/2/7 18:58
 */
public class DrillProxy extends AnnotationProxy<Drill, Erupt> {

    @Override
    protected Object invocation(MethodInvocation invocation) {
        if (super.matchMethod(invocation, Drill::code) && AnnotationConst.EMPTY_STR.equals(this.rawAnnotation.code())) {
            return Integer.toString(this.rawAnnotation.title().hashCode());
        } else if (super.matchMethod(invocation, Drill::title)) {
            return ProxyContext.translate(this.rawAnnotation.title());
        }
        return this.invoke(invocation);
    }

}
