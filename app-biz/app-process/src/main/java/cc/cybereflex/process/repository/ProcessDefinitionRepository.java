package cc.cybereflex.process.repository;

import cc.cybereflex.process.model.data.ProcessDefinition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcessDefinitionRepository {

    List<ProcessDefinition> queryAll();



}
