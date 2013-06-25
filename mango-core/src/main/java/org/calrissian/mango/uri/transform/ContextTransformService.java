/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.uri.transform;

import com.google.common.net.MediaType;
import org.calrissian.mango.uri.domain.ResolvedItem;
import org.calrissian.mango.uri.exception.ContextTransformException;
import org.calrissian.mango.uri.transform.interceptor.ContextTransformInterceptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContextTransformService {

    private final Map<String, ContextTransformer> contextTransformMap = new HashMap<String,ContextTransformer>();
    private final Map<Class, ContextTransformInterceptor> transformInterceptorMap = new HashMap<Class,ContextTransformInterceptor>();

    public ContextTransformService(Collection<ContextTransformer> contextTransforms,
                                   Collection<ContextTransformInterceptor> contextTransformInterceptors) {

        if(contextTransforms != null) {
            for(ContextTransformer transform : contextTransforms) {
                contextTransformMap.put(transform.getContextName(), transform);
            }
        }

        if(contextTransformInterceptors != null) {
            for(ContextTransformInterceptor interceptor : contextTransformInterceptors) {
                transformInterceptorMap.put(interceptor.intercepts(), interceptor);
            }
        }

        System.out.println(contextTransforms);
        System.out.println(contextTransformInterceptors);
    }

    public ResolvedItem transform(String contextName, Object obj) throws ContextTransformException {

        ContextTransformInterceptor interceptor = transformInterceptorMap.get(obj.getClass());

        if(interceptor != null) {

            return interceptor.transform(obj);
        }

        else {

            ContextTransformer transform = contextTransformMap.get(contextName);
            return transform.transform(obj);
        }
    }

    public MediaType getMediaType(String contextName, Object obj) throws ContextTransformException {

        ContextTransformInterceptor interceptor = transformInterceptorMap.get(obj.getClass());

        if(interceptor != null) {

            return interceptor.getMediaType(obj);
        }

        else {

            ContextTransformer transform = contextTransformMap.get(contextName);
            return transform.getMediaType(obj);
        }

    }
}
