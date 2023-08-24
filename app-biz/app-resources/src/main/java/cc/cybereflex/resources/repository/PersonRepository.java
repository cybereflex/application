package cc.cybereflex.resources.repository;

import cc.cybereflex.resources.model.data.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonRepository {

    List<Person> queryAllAvailable();

    Person queryByUsername(String username);

    void insert(Person person);


}
