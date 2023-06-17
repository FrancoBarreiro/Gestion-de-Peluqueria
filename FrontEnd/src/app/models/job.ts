import { Subjob } from "./subjob";

export class Job {

    idJob:Number;
    idClient:Number;
    jobTitle:String;
    jobDescription:String;
    totalAmount:number = 0;
    date:string;
    subJobs: Subjob[];

    constructor(){}

}
