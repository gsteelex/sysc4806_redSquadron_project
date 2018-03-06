package learningOutcomes.repositories;

import learningOutcomes.Program;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    @Test
    public void testFindById() {
        Program program = new Program();

        programRepository.save(program);

        assertTrue(programRepository.findById(program.getId()).isPresent());
        assertEquals(program, programRepository.findById(program.getId()).get());
    }

}
