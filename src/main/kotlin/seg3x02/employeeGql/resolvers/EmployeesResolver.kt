package seg3x02.employeeGql.resolvers

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import java.util.*

@Controller
class EmployeesResolver(
    private val employeeRepository: EmployeesRepository,
    private val mongoOperations: MongoOperations
) {

    // Query to retrieve all employees
    @QueryMapping
    fun employees(): List<Employee> {
        return employeeRepository.findAll() ?: emptyList()
    }

    // Mutation to add a new employee
    @MutationMapping
    fun addEmployee(@Argument("createEmployeeInput") input: CreateEmployeeInput): Employee {
        if (input.name!!.isNotBlank() &&
            input.dateOfBirth!!.isNotBlank() &&
            input.city!!.isNotBlank() &&
            input.salary!! >= 0 &&
            input.gender!!.isNotBlank() &&
            input.email!!.isNotBlank()
        ) {
            val employee = Employee(
                name = input.name,
                dateOfBirth = input.dateOfBirth,
                city = input.city,
                salary = input.salary,
                gender = input.gender,
                email = input.email
            )
            employee.id = UUID.randomUUID().toString()
            employeeRepository.save(employee)
            return employee
        } else {
            throw IllegalArgumentException("Invalid input")
        }
    }
}
