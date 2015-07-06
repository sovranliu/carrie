package com.dianping.midasx.base.xml.core;

import com.dianping.midasx.base.type.core.IMapping;
import com.dianping.midasx.base.type.core.ISet;

/**
 * XML节点集接口
 */
public interface IXMLNodeSet extends ISet<IXMLNode>, IMapping<String, ISet<IXMLNode>> { }
