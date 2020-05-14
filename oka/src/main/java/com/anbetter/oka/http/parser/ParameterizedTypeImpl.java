package com.anbetter.oka.http.parser;

import androidx.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * <p>
 * Created by android_ls on 2020/3/17 2:32 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class ParameterizedTypeImpl implements ParameterizedType {

    private final Type rawType;
    private final Type ownerType;
    private final Type[] actualTypeArguments;

    /**
     * 适用于单个泛型参数的类
     * @param rawType
     * @param actualType
     */
    public ParameterizedTypeImpl(Type rawType, Type actualType) {
        this(null, rawType, actualType);
    }

    /**
     * 适用于多个泛型参数的类
     * @param ownerType
     * @param rawType
     * @param actualTypeArguments
     */
    public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... actualTypeArguments) {
        this.rawType = rawType;
        this.ownerType = ownerType;
        this.actualTypeArguments = actualTypeArguments;
    }

    /**
     * 本方法仅使用于单个泛型参数的类
     * 根据types数组，确定具体的泛型类型
     * List里面是List  对应  get(List.class, List.class, String.class)
     *
     * @param rawType Type
     * @param types   Type数组
     * @return ParameterizedTypeImpl
     */
    public static ParameterizedTypeImpl get(@NonNull Type rawType, @NonNull Type... types) {
        final int length = types.length;
        if (length > 1) {
            Type parameterizedType = new ParameterizedTypeImpl(types[length - 2], types[length - 1]);
            Type[] newTypes = Arrays.copyOf(types, length - 1);
            newTypes[newTypes.length - 1] = parameterizedType;
            return get(rawType, newTypes);
        }
        return new ParameterizedTypeImpl(rawType, types[0]);
    }

    /**
     * 适用于多个泛型参数的类
     * @param rawType
     * @param actualTypeArguments
     * @return
     */
    public static ParameterizedTypeImpl getParameterized(@NonNull Type rawType, @NonNull Type... actualTypeArguments) {
        return new ParameterizedTypeImpl(null, rawType, actualTypeArguments);
    }

    @NonNull
    @Override
    public final Type[] getActualTypeArguments() {
//        MLog.i("actualTypeArguments[0] = " + actualTypeArguments[0]);
//        MLog.i("actualTypeArguments[1] = " + actualTypeArguments[1]);
        return actualTypeArguments;
    }

    public final Type getOwnerType() {
//        MLog.i("ownerType = " + ownerType);
        return ownerType;
    }

    @NonNull
    @Override
    public final Type getRawType() {
        // 返回最外层<>前面那个类型，即Map<K ,V>的Map。
//        MLog.i("rawType" + rawType);
        return rawType;
    }

}
