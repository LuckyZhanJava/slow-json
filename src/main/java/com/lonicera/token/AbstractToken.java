package com.lonicera.token;

/**
 * @author LiBowei
 * @date 2020年-12月-07日
 **/
public abstract class AbstractToken implements Token{

	@Override
	public String toString(){
		return getClass() + ":[" + text() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return ((AbstractToken)obj).text().equals(text());
	}

}
