package com.example.app.dao
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import java.io.Serializable

@TableName("`user`")
data class UserPO (
    @TableId(value="id", type= IdType.AUTO)
    var id: Int ?,
    var username: String ?,
    var password: String ?
)
    : Serializable

@Mapper
interface UserMapper : BaseMapper<UserPO> {
}
