import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAddAdminComponent } from './edit-add-admin.component';

describe('EditAddAdminComponent', () => {
  let component: EditAddAdminComponent;
  let fixture: ComponentFixture<EditAddAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditAddAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditAddAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
