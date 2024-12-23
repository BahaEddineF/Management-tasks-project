import { Component, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormControl, FormGroup } from '@angular/forms';
import { UserService } from '../../../../services/users/user.service';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.scss']
})
export class ImageUploadComponent {

  email: string | null;  // Change from String to string
  @Output() imageUploaded = new EventEmitter<string>();

  uploadForm: FormGroup;
  selectedFile: File | null = null;

  constructor(private http: HttpClient, private userservice: UserService) {
    this.uploadForm = new FormGroup({
      image: new FormControl(null)
    });
    this.email = localStorage.getItem('user_email');  // This is string | null
  }

  onFileSelect(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  onUpload() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile, this.selectedFile.name);
  
      if (this.email != null) {
        this.userservice.uploadImage(this.email, formData).subscribe({
          next: (response: any) => {
            // Show success alert
            alert('Image uploaded successfully!');
  
            // Emit the image URL
            this.imageUploaded.emit(response.imageUrl);
  
            // Close the dialog
  
            // Reload the page
            window.location.reload();
          },
          error: (error) => {
            console.error('Error uploading image:', error);
            alert('Error uploading image. Please try again.');
          }
        });
      }
    }
  }
  
  
  
  
}
