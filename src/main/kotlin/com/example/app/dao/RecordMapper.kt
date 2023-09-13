package com.example.app.dao

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import java.io.Serializable
import java.math.BigDecimal

@TableName("`record`")
data class RecordPO(
    @TableId(value = "id", type = IdType.AUTO)
    var id: Int?,
    var userId: Int?,
    var amount: BigDecimal?,
    var ctime: String?,
    var utime: String?,
) : Serializable

@Mapper
interface RecordMapper : BaseMapper<RecordPO> {
}
