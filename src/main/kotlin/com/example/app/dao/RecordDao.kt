package com.example.app.dao

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.math.BigDecimal

interface RecordDao  : IService<RecordPO>{

}

@Repository
class RecordDaoImpl : ServiceImpl<RecordMapper, RecordPO>() , RecordDao {
}