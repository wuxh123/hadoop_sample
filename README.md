# Hbase_sample
使用python测试hbase的例子

# hbase环境、虚拟机下载
https://github.com/wuxh123/hadoop_text

# python通过thrift操作hbase实例

thrift 是facebook开发并开源的一个二进制通讯中间件，通过thrift，我们可以充分利用各个语言的优势，编写高效的代码。

  

关于thrift的论文：http://pan.baidu.com/share/link?shareid=234128&uk=3238841275

  

安装thrift：http://thrift.apache.org/docs/install/ubuntu/

  

安装完成后到hbase的目录下，找到Hbase.thrift，该文件在

  

hbase-0.94.4/src/main/resources/org/apache/hadoop/hbase/thrift下可以找到

  

thrift --gen python hbase.thrift 会生成gen-py文件夹，将其修改成hbase

  

安装python的thrift库

  

sudo pip install thrift

  

启动hbase的thrift服务：bin/hbase-daemon.sh start thrift 默认端口是9090

  

创建hbase表:

    
    
    from thrift import Thrift
    from thrift.transport import TSocket
    from thrift.transport import TTransport
    from thrift.protocol import TBinaryProtocol
     
    from hbase import Hbase
    from hbase.ttypes import *
     
    transport = TSocket.TSocket('localhost', 9090);
     
    transport = TTransport.TBufferedTransport(transport)
     
    protocol = TBinaryProtocol.TBinaryProtocol(transport);
     
    client = Hbase.Client(protocol)
    transport.open()
     
     
    contents = ColumnDescriptor(name='cf:', maxVersions=1)
    client.createTable('test', [contents])
     
    print client.getTableNames()

执行代码，成功后，进入hbase的shell，用命令list可以看到刚刚的test表已经创建成功。

插入数据：

    
    
    from thrift import Thrift
    from thrift.transport import TSocket
    from thrift.transport import TTransport
    from thrift.protocol import TBinaryProtocol
     
    from hbase import Hbase
     
    from hbase.ttypes import *
     
    transport = TSocket.TSocket('localhost', 9090)
     
    transport = TTransport.TBufferedTransport(transport)
     
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
     
    client = Hbase.Client(protocol)
     
    transport.open()
     
    row = 'row-key1'
     
    mutations = [Mutation(column="cf:a", value="1")]
    client.mutateRow('test', row, mutations, None)

获取一行数据：

    
    
    from thrift import Thrift
    from thrift.transport import TSocket
    from thrift.transport import TTransport
    from thrift.protocol import TBinaryProtocol
     
    from hbase import Hbase
    from hbase.ttypes import *
     
    transport = TSocket.TSocket('localhost', 9090)
    transport = TTransport.TBufferedTransport(transport)
     
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
     
    client = Hbase.Client(protocol)
     
    transport.open()
     
    tableName = 'test'
    rowKey = 'row-key1'
     
    result = client.getRow(tableName, rowKey, None)
    print result
    for r in result:
        print 'the row is ' , r.row
        print 'the values is ' , r.columns.get('cf:a').value

返回多行则需要使用scan：

    
    
    from thrift import Thrift
    from thrift.transport import TSocket
    from thrift.transport import TTransport
    from thrift.protocol import TBinaryProtocol
     
    from hbase import Hbase
    from hbase.ttypes import *
     
    transport = TSocket.TSocket('localhost', 9090)
    transport = TTransport.TBufferedTransport(transport)
     
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
     
    client = Hbase.Client(protocol)
    transport.open()
     
    scan = TScan()
    tableName = 'test'
    id = client.scannerOpenWithScan(tableName, scan, None)
     
    result2 = client.scannerGetList(id, 10)
     
    print result2

scannerGet则是每次只取一行数据：

    
    
    from thrift import Thrift
    from thrift.transport import TSocket
    from thrift.transport import TTransport
    from thrift.protocol import TBinaryProtocol
     
    from hbase import Hbase
    from hbase.ttypes import *
     
    transport = TSocket.TSocket('localhost', 9090)
    transport = TTransport.TBufferedTransport(transport)
     
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
     
    client = Hbase.Client(protocol)
    transport.open()
     
    scan = TScan()
    tableName = 'test'
    id = client.scannerOpenWithScan(tableName, scan, None)
    result = client.scannerGet(id)
    while result:
        print result
        result = client.scannerGet(id)
