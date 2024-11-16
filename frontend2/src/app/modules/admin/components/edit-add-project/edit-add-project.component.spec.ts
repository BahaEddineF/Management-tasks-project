import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAddProjectComponent } from './edit-add-project.component';

describe('EditAddProjectComponent', () => {
  let component: EditAddProjectComponent;
  let fixture: ComponentFixture<EditAddProjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditAddProjectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditAddProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
