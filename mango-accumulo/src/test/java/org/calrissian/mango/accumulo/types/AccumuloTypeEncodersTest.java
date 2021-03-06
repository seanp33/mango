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
package org.calrissian.mango.accumulo.types;

import org.calrissian.mango.domain.IPv4;
import org.calrissian.mango.types.TypeEncoder;
import org.calrissian.mango.types.exception.TypeDecodingException;
import org.calrissian.mango.types.exception.TypeEncodingException;
import org.junit.Test;

import java.net.URI;
import java.util.Date;


import static org.calrissian.mango.accumulo.types.AccumuloTypeEncoders.*;
import static org.junit.Assert.assertEquals;

public class AccumuloTypeEncodersTest {

    private static<T> void verifyBasicFunctionality(String alias, T testObject, TypeEncoder<T, String> encoder) throws TypeEncodingException, TypeDecodingException {
        assertEquals(alias, encoder.getAlias());
        assertEquals(testObject.getClass(), encoder.resolves());

        //test encode decode returns same value
        assertEquals(testObject, encoder.decode(encoder.encode(testObject)));
    }

    @Test
    public void testBasicFunctionality() throws Exception{
        verifyBasicFunctionality("boolean", true, booleanEncoder());
        verifyBasicFunctionality("byte", (byte) 3, byteEncoder());
        verifyBasicFunctionality("date", new Date(), dateEncoder());
        verifyBasicFunctionality("double", -1.5D, doubleEncoder());
        verifyBasicFunctionality("float", -1.5F, floatEncoder());
        verifyBasicFunctionality("integer", 3, integerEncoder());
        verifyBasicFunctionality("ipv4", new IPv4("192.168.1.1"), ipv4Encoder());
        verifyBasicFunctionality("long", 3L, longEncoder());
        verifyBasicFunctionality("string", "testing", stringEncoder());
        verifyBasicFunctionality("uri", new URI("http://testing.org"), uriEncoder());

        verifyBasicFunctionality("boolean", true, booleanRevEncoder());
        verifyBasicFunctionality("byte", (byte) 3, byteRevEncoder());
        verifyBasicFunctionality("date", new Date(), dateRevEncoder());
        verifyBasicFunctionality("double", -1.5D, doubleRevEncoder());
        verifyBasicFunctionality("float", -1.5F, floatRevEncoder());
        verifyBasicFunctionality("integer", 3, integerRevEncoder());
        verifyBasicFunctionality("ipv4", new IPv4("192.168.1.1"), ipv4RevEncoder());
        verifyBasicFunctionality("long", 3L, longRevEncoder());
    }

    @Test
    public void testCorrectEncoding () throws Exception {

        assertEquals("1", booleanEncoder().encode(true));
        assertEquals("0", booleanEncoder().encode(false));

        assertEquals("03", byteEncoder().encode((byte) 3));

        assertEquals("800000000000000a", dateEncoder().encode(new Date(10)));

        assertEquals("bff8000000000000", doubleEncoder().encode(1.5D));
        assertEquals("4007ffffffffffff", doubleEncoder().encode(-1.5D));

        assertEquals("bfc00000", floatEncoder().encode(1.5F));
        assertEquals("403fffff", floatEncoder().encode(-1.5F));

        assertEquals("80000003", integerEncoder().encode(3));
        assertEquals("7ffffffd", integerEncoder().encode(-3));

        assertEquals("c0a80101", ipv4Encoder().encode(new IPv4("192.168.1.1")));
        assertEquals("ffffffff", ipv4Encoder().encode(new IPv4("255.255.255.255")));

        assertEquals("8000000000000003", longEncoder().encode(3L));
        assertEquals("7ffffffffffffffd", longEncoder().encode(-3L));

        assertEquals("test", stringEncoder().encode("test"));

        assertEquals("http://testing.org", uriEncoder().encode(new URI("http://testing.org")));
    }

    @Test
    public void testCorrectReverseEncoding () throws Exception {

        assertEquals("0", booleanRevEncoder().encode(true));
        assertEquals("1", booleanRevEncoder().encode(false));

        assertEquals("fc", byteRevEncoder().encode((byte) 3));

        assertEquals("7ffffffffffffff5", dateRevEncoder().encode(new Date(10)));

        assertEquals("4007ffffffffffff", doubleRevEncoder().encode(1.5D));
        assertEquals("bff8000000000000", doubleRevEncoder().encode(-1.5D));

        assertEquals("403fffff", floatRevEncoder().encode(1.5F));
        assertEquals("bfc00000", floatRevEncoder().encode(-1.5F));

        assertEquals("7ffffffc", integerRevEncoder().encode(3));
        assertEquals("80000002", integerRevEncoder().encode(-3));

        assertEquals("3f57fefe", ipv4RevEncoder().encode(new IPv4("192.168.1.1")));
        assertEquals("00000000", ipv4RevEncoder().encode(new IPv4("255.255.255.255")));

        assertEquals("7ffffffffffffffc", longRevEncoder().encode(3L));
        assertEquals("8000000000000002", longRevEncoder().encode(-3L));
    }
}
