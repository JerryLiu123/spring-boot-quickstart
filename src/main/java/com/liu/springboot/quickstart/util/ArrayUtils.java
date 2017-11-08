package com.liu.springboot.quickstart.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.ibatis.reflection.ArrayUtil;

public class ArrayUtils extends ArrayUtil{

	@SuppressWarnings("unchecked")
	public static <T> T[] concat(T[] a, T[] b) {  

	    final int alen = a.length;  
	    final int blen = b.length;  
	    if (alen == 0) {  
	        return b;  
	    }  
	    if (blen == 0) {  
	        return a;  
	    }  

	    final T[] result = (T[]) java.lang.reflect.Array.  
	            newInstance(a.getClass().getComponentType(), alen + blen);  

	    System.arraycopy(a, 0, result, 0, alen);  
	    System.arraycopy(b, 0, result, alen, blen);  
	    return result;  
	} 
	
	/**
	 * 对象转 byte[]
	 * @param obj
	 * @return
	 * @throws Exception
	 */
    public static byte[] objectToBytes(Object obj) throws Exception{
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        byte[] bytes = bo.toByteArray();
        bo.close();
        oo.close();
        return bytes;
    }
    /**byte[]转对象
     * @param bytes
     * @return
     * @throws Exception
     */
    public static Object bytesToObject(byte[] bytes) throws Exception{
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = new ObjectInputStream(in);
        return sIn.readObject();
    }
}