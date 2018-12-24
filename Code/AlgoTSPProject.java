package algo;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Adil Ayub
 */
public class AlgoTSPProject {

    public int [][] readdata(File file_name,int city_size) throws FileNotFoundException
    {
        Scanner sc = new Scanner(file_name);int temp;
        int data[][]= new int [city_size][city_size];
        for(int i=0;i<city_size;i++)
        {
            for(int j=0;j<city_size;j++)
            {
                temp=sc.nextInt();
                if(temp == 0)
                    break;
                else
                    data[i][j]=temp;
            }
        }
        return data;
    }
    public int [][] shuffle(int data[][],int index)
    {
        Random seed = new Random();int temp;
        int range,range2;
        for(int i=0;i<data[0].length;i++)
        {
            data[index+1][i]=data[index][i];
        }
        for(int i=0;i<5;i++)
        {
            range=seed.nextInt((data[0].length)-2) + 1;
            range2=seed.nextInt((data[0].length)-2) + 1;
            temp=data[index+1][range];
            data[index+1][range]=data[index+1][range2];
            data[index+1][range2]=temp;
        }
        return data;
    }
    public int [][] makechromones(int size)
    {
        int data[][]=new int [20][size+1];
        for(int i=0;i<size;i++)
        {
            data[0][i]=i;
        }
        for(int i=0;i<19;i++)
        {
            data=shuffle(data, i);
        }
        return data;
    }
    public double [] fitness(int data[][],int chromones[][])
    {
        int city1,city2;double getdist,total=0;double fitness[]=new double [20];double temp;int temp2;
        for(int i=0;i<chromones.length;i++)
        {
            for(int j=0;j<(chromones[0].length)-1;j++)
            {
                city1=chromones[i][j];city2=chromones[i][j+1];
               
                if(city1>city2){
                    temp2=city2;
                    city2=city1;
                    city1=temp2;
                }
               
                getdist=data[city2][city1];
                total=total+getdist;
            }
            temp=total;
            fitness[i]=temp;
            total=0;
        }
        return fitness;
    }
    
    public void debug(int newdata[][])
    {
        String temp;
        for(int i=0;i<newdata.length;i++)
        {
            for(int j=0;j<newdata[0].length;j++)
            {
                temp=Integer.toString(newdata[i][j]);
                System.out.print(temp+'\t');
            }
            System.out.printf("\n");
        }   
    }
    public int [][] Tournment_Selection(double fitness[],int city,int chromones[][])
    {
        Random seed = new Random();int selected[]=new int [20],num,selected_population[][]= new int [10][city+1],parents [][]=new int [2][city+1];
        double new_fitness[]= new double [10],best_fit_1=0,best_fit_2=0;int p1=0,p2=0;int replace =0;
        for(int i=0;i<10;i++)
        {
            num=seed.nextInt(20);
            if(selected[num] == 0)
            {
                selected[num]=1;
                for(int j=0;j<chromones[0].length;j++)
                {
               //     System.out.println(num);
                    selected_population[i][j]=chromones[num][j];
                    new_fitness[i]=fitness[num];
                }
            }
            else
                i--;
        }
        for(int i=0;i<10;i++)
        {
            if(new_fitness[i] > best_fit_1)
            {
                best_fit_1=new_fitness[i];
                p1=i;
            }
        }
        for(int i=0;i<10;i++)
        {
            if((new_fitness[i] > best_fit_2) && i != p1)
            {
                best_fit_2=new_fitness[i];
                p2=i;
                
            }
        }
        for(int i=0;i<city+1;i++)
        {
            parents[0][i]=selected_population[p1][i];
            parents[1][i]=selected_population[p2][i];       
        }
        return parents;
    }
    
    
    public int [][] CX(int [][] parants){
         int [][] offspring=new int [2][parants[0].length]; int num=0, index=0;
         boolean check=false;
         for(int i=0; i<2; i++){
            offspring[i][0]=parants[i][0];
            int p=0;
            while(true){
            //for(int temp=0; temp<offspring[0].length*2; i++){
                if(i==0){
                    num = parants[i+1][p];
                }
                else{
                    num = parants[i-1][p];
                }
                for(int j=0; j<parants[0].length; j++){
                    if(parants[i][j]==num &&  offspring[i][j]!=num){
                        check=true;
                        index=j;
                    }
                }
                if(check){
                    offspring[i][index]=num;
                    p=index;
                }
                else{
                    break;
                }
                check=false;
            }
            for(int j=0; j<offspring[0].length; j++){
                if(offspring[i][j]==0){
                    if(i==0){
                        offspring[i][j] = parants[i+1][j];
                    }
                    else{
                        offspring[i][j] = parants[i-1][j];
                    }
                }
            }
         }
         
        return offspring;
         
    }
    
    
    
    
    
    public int [][] OX (int [][] parants){
       
        int [][] offspring=new int [2][parants[0].length];  //extracting middle elements
        int ratio=(parants[0].length)/3;
        for(int i=0; i<2; i++){
            for(int j=0; j<parants[0].length; j++){
                if(j>=ratio && j<=(ratio*2)-1){
                    offspring[i][j]=parants[i][j];
                }
            }
        }
        
        
        
        int col=ratio*2, rep [][]=new int [2][parants[0].length], row=0;
        for(int i=0; i<2; i++){
            for(int j=0; j<parants[0].length; j++){                //extracting rem data
                if(i==0){
                    row=1;
                }
                else{
                    row=0;
                }
                if(col==parants[0].length){
                    col=0;
                }
                rep[i][j]=parants[row][col];
                col+=1;
            }
        }
         
        
        for(int i=0; i<2; i++){
            for(int j=0; j<parants[0].length; j++){           //deleting alteady taken data
                for(int k=ratio; k<ratio*2; k++){
                    if(rep[i][j]==parants[i][k]){  
                        rep[i][j]=0;
                    }
                }    
            }    
        }
        
        
        for (int i=0; i<2; i++){
            col=ratio*2; int x=-1;
            for (int j=0; j<parants[0].length; j++){        //passing remaining data in offsprings
                if(col==parants[0].length){
                    col=0;
                }
                if (col==ratio){
                    break;
                }
                for(int k=x+1; k<rep[0].length; k++){
                    if(rep[i][k]!=0){
                        //System.out.print("rep:"+rep[i][k]+" ");
                        x=k;
                        break;
                        //rep[i][k]=0;
        
                    }
                }
                if(offspring[i][col]==0){
                    offspring[i][col]=rep[i][x];
                    col+=1;
                }
            }
        }
      /*  
        for(int i=0; i<offspring.length; i++){
            for (int j=0; j<offspring[0].length; j++){
                System.out.print(offspring[i][j]+"\t");
            }
            System.out.println();
        } 
*/

        return offspring;
    }
  
    public int [][] swapMutation(int offspring[][]) {
        for(int i=0; i<2; i++){
            int a=(int) (Math.random()*(offspring[0].length));
            int b=(int) (Math.random()*(offspring[0].length));
            while(b==a){
                b=(int) (Math.random()*(offspring[0].length));
            }
            
          //  System.out.println("size:"+offspring[0].length);
          //  System.out.println("a:"+a+"\nb:"+b);
            int temp=offspring[i][a];
            offspring[i][a]=offspring[i][b];
            offspring[i][b]=temp;        
        }
        return offspring;
    }
    
    public double [] fitnessOverall(int data[][],int chromones[][]){
        int city1,city2;double getdist,total=0;double fitness[]=new double [20];double temp=0; int temp2=0;
        for(int i=0;i<chromones.length;i++)
        {
            for(int j=0;j<(chromones[0].length)-1;j++)
            {
                city1=chromones[i][j];city2=chromones[i][j+1];
                
                
                if(city1>city2){
                    temp2=city2;
                    city2=city1;
                    city1=temp2;
                }
                
                
                getdist=data[city2][city1];
                total=total+getdist;
            }
            temp=total;
            fitness[i]=temp;
            total=0;
        }
        return fitness;
    }
            
    public static void main(String[] args) throws FileNotFoundException 
    {   
       for(int pp=0; pp<20; pp++){
       AlgoTSPProject obj=new AlgoTSPProject();
       
        int data[][], chromones[][], parents[][], offspringsTemp[][];
        double fitness[];
        
        
        File file = new File("D:\\gr24.txt"); 
        int no_C=24;
        data=obj.readdata(file,no_C);
        
        chromones=obj.makechromones(no_C);
        
        
        int newPop[][]=new int [chromones.length][chromones[0].length];
        
        for(int gens=0; gens<5000; gens++){
            
            for(int m=0; m<chromones.length/2; m++){
                
                fitness=obj.fitness(data, chromones);
                parents=obj.Tournment_Selection(fitness,no_C,chromones);
                
                
                
                int tempParent[][]=new int [2][parents[0].length-2];
                
                for(int i=0; i<2; i++){
                    for(int j=1; j<parents[0].length-1; j++){
                        tempParent[i][j-1]=parents[i][j];
                    }
                }

                offspringsTemp=obj.CX(tempParent);

                int MutatedOffspring[][]= obj.swapMutation(offspringsTemp);

                int offsprings[][]=new int [2][MutatedOffspring[0].length+2];


                for(int i=0; i<2; i++){
                    for(int j=0; j<MutatedOffspring[0].length; j++){
                            offsprings[i][j+1]=MutatedOffspring[i][j];
                    }
                }
                int row=0;
                
                for(int i=0; i<newPop.length; i++){
                    if(newPop[i][1]==0){
                        int newRow=i;
                        for(int j=0; j<2; j++){
                            for(int k=0; k<offsprings[0].length; k++){
                                newPop[newRow][k]=offsprings[row][k];
                            }
                            row+=1;
                            newRow+=1;
                        }
                        break;
                    }
                }
                
            }
            
        }
        
        double fitness1[];
        fitness1 =obj.fitnessOverall(data, chromones);
        double min=fitness1[0];
        int var=0;
        for(int i=0; i<fitness1.length; i++){
            if(fitness1[i]<min){
                min=fitness1[i];
                var=i;
            }
        }
        System.out.println(min);
            
    }
    }
}
