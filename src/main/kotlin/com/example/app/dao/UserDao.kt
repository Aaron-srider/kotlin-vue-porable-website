package com.example.app.dao

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.app.utils.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.math.BigDecimal

interface UserDao  : IService<UserPO>{
    fun getUser(id: Serializable): UserPO?

    fun getUsers(): MutableList<UserPO>;

    fun getUserRecords(userId: Serializable): MutableList<RecordPO>;

    fun addRecord(userId: Serializable, amount: BigDecimal)

    fun removeRecord(userId: Serializable, recordId: Serializable)

    fun updateRecord(userId: Serializable, recordId: Serializable, amount: BigDecimal)

}

@Repository
class UserDaoImpl : ServiceImpl<UserMapper, UserPO>() , UserDao {
    @Autowired
    lateinit var recordDao: RecordDao

    override fun getUser(id: Serializable): UserPO? {
        return getById(id)
    }

    override fun getUsers(): MutableList<UserPO> {
        return list()
    }

    override fun getUserRecords(userId: Serializable): MutableList<RecordPO> {
        return recordDao.list(QueryWrapper<RecordPO>().eq("user_id", userId))
    }

    override fun addRecord(userId: Serializable, amount: BigDecimal) {
        var newRocord = RecordPO(
            id = null,
            userId = userId as Int,
            amount = amount,
            ctime = DateTime.now().toString(),
            utime = DateTime.now().toString(),
        )
        recordDao.save(newRocord)
    }

    override fun removeRecord(userId: Serializable, recordId: Serializable) {
        recordDao.remove(QueryWrapper<RecordPO>().eq("id", recordId).eq("user_id", userId))
    }

    override fun updateRecord(userId: Serializable, recordId: Serializable, amount: BigDecimal) {
        recordDao.updateById(RecordPO(
            id = recordId as Int,
            userId = userId as Int,
            amount = amount,
            ctime = null,
            utime = DateTime.now().toString(),
        ))
    }
}