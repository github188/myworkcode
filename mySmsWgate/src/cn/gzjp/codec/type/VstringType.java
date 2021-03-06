package cn.gzjp.codec.type;

import java.nio.ByteBuffer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import cn.gzjp.codec.DecodeException;
import cn.gzjp.codec.Decoder;
import cn.gzjp.codec.EncodeException;
import cn.gzjp.codec.Encoder;
import cn.gzjp.codec.Type;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vstringType")
public class VstringType implements Type,Encoder,Decoder{

    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected String vtype;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String value) {
        this.vtype = value;
    }

	@Override
	public String getTypeName() {
		return "String";
	}
	
	@Override
	public ByteBuffer encode(Object o) throws EncodeException {
		checkEncodeType(vtype);
		String data=(String)o;		
		int len=data.getBytes().length;
		ByteBuffer buffer=ByteBuffer.allocate(getTypeLength(vtype)+len);
		if("byte".equals(vtype))buffer.put((byte)len);
		if("int".equals(vtype))buffer.putInt(len);
		buffer.put(data.getBytes());
		return buffer;
	}


	@Override
	public Object decode(ByteBuffer buf) throws DecodeException {
		checkDecodeType(vtype);
		int len=0;
		if("byte".equals(vtype))len=buf.get();
		if("int".equals(vtype))len=buf.getInt();
		byte[]data=new byte[len];
		buf.get(data);
		return new String(data);
	}
	
	private void checkEncodeType(String type) throws EncodeException {
		if("byte".equals(type))return;
		if("int".equals(type))return;
		throw new EncodeException("unrecognizable the vstring vtype ["+type+"] of "+name);
	}
	
	private void checkDecodeType(String type) throws DecodeException {
		if("byte".equals(type))return;
		if("int".equals(type))return;
		throw new DecodeException("unrecognizable the vstring vtype ["+type+"] of "+name);
	}
	
	private int getTypeLength(String type) throws EncodeException {
		if("byte".equals(type))return 1;
		if("int".equals(type))return 4;
		throw new EncodeException("not support type["+type+"]");
	}

}
