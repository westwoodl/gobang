package com.xrc.gb.work.weiqi;

import com.xrc.gb.enums.GameTypeEnum;
import com.xrc.gb.enums.PlaceResultTypeEnum;
import com.xrc.gb.manager.go.dto.GoContext;
import com.xrc.gb.work.AbstractGoGameRunner;
import com.xrc.gb.work.GoGameFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author xu rongchao
 * @date 2020/3/26 8:53
 */
@Component
public class WeiqiGameRunner extends AbstractGoGameRunner implements InitializingBean {



    @Override
    public int[][] buildArrays(GoContext goContext) {
        return new int[0][];
    }

    @Override
    public boolean judgeLast(int[][] goArrays, int lastX, int lastY) {
        return false;
    }

    @Override
    public boolean judgeAll(int[][] goArrays) {
        return false;
    }

    /**
     * spring bean的初始化执行顺序：构造方法 --> @PostConstruct注解的方法 --> afterPropertiesSet方法 --> init-method指定的方法。
     * afterPropertiesSet通过接口实现方式调用（效率上高一点），@PostConstruct和init-method都是通过反射机制调用
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        GoGameFactory.register(GameTypeEnum.WEI_QI, this);
    }
}
