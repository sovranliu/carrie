package com.slfuture.carrie.base.xml.core;

import com.slfuture.carrie.base.type.core.IMapping;
import com.slfuture.carrie.base.type.core.ISet;

/**
 * XML节点集接口
 */
public interface IXMLNodeSet extends ISet<IXMLNode>, IMapping<String, ISet<IXMLNode>> { }
