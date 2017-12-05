import {Vehicle} from "./vehicle";
export class Todo {
  public id: string;
  public vehicleTypes: Array<Vehicle> = [new Vehicle(-1,'--Select Vehicle--'),
                    new Vehicle(1,'Ambulance'),new Vehicle(2, 'Fire Truck'),new Vehicle(3, 'Police Car')];
  public vehicleType: string;
  public completed: boolean;
  public createdAt: Date;
  public zipCode: string;
  public vehicleId: string;
  public distance: string;
  public requestId: string;
  public source: string;
  public path:string;
}
