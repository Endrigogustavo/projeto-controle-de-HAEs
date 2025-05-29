export interface Employee {
    id: string;
    name: string;
    email: string;
    role: string; 
}

export interface GetProfessorResponse {
    message: string;
    record: Employee;
}